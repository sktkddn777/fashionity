package com.infinity.fashionity.consultants.service;

import com.infinity.fashionity.comments.dto.CommentDeleteDTO;
import com.infinity.fashionity.comments.dto.CommentSaveDTO;
import com.infinity.fashionity.consultants.dto.*;
import com.infinity.fashionity.consultants.entity.*;
import com.infinity.fashionity.consultants.repository.ConsultantRepository;
import com.infinity.fashionity.consultants.repository.ReservationRepository;
import com.infinity.fashionity.consultants.repository.ReviewRepository;
import com.infinity.fashionity.consultants.repository.ScheduleRepository;
import com.infinity.fashionity.global.exception.AccessDeniedException;
import com.infinity.fashionity.global.exception.ErrorCode;
import com.infinity.fashionity.global.exception.NotFoundException;
import com.infinity.fashionity.global.exception.ValidationException;
import com.infinity.fashionity.global.utils.HashUtil;
import com.infinity.fashionity.global.utils.StringUtils;
import com.infinity.fashionity.image.dto.ImageDTO;
import com.infinity.fashionity.image.dto.ImageSaveDTO;
import com.infinity.fashionity.image.service.ImageService;
import com.infinity.fashionity.members.data.MemberRole;
import com.infinity.fashionity.members.entity.MemberEntity;
import com.infinity.fashionity.members.entity.MemberRoleEntity;
import com.infinity.fashionity.members.exception.MemberNotFoundException;
import com.infinity.fashionity.members.repository.MemberRepository;
import com.infinity.fashionity.members.repository.MemberRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLOutput;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.infinity.fashionity.global.exception.ErrorCode.MEMBER_NOT_FOUND;
import static com.infinity.fashionity.global.exception.ErrorCode.RESERVATION_NOT_FOUND;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ConsultantServiceImpl implements ConsultantService {
    private final ConsultantRepository consultantRepository;
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final ScheduleRepository scheduleRepository;
    private final ImageService imageService;

    // [공통] 컨설턴트 목록 조회
    @Override
    @Transactional(readOnly = true)
    public ConsultantListDTO.Response getAllConsultants(ConsultantListDTO.Request dto) {

        int page = dto.getPage();
        int size = dto.getSize();
        List<ConsultantSummary> consultantSummaries = new ArrayList<>();

        // 내림차순 정렬
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        // 최신순으로 컨설턴트 가져오기
        Page<ConsultantEntity> result = consultantRepository.findAll(pageable);


        result.stream().forEach(entity -> {
            ConsultantSummary consultantSummary = ConsultantSummary.builder()
                    .seq(entity.getSeq())
                    .nickname(entity.getNickname())
                    .profileUrl(entity.getMember().getProfileUrl())
                    .level(entity.getLevel())
                    .avgGrade(consultantRepository.avgGrade(entity.getNickname()))
                    .totalCnt(consultantRepository.totalCnt(entity.getNickname()))
                    .profileIntro(entity.getMember().getProfileIntro())
                    .build();
            consultantSummaries.add(consultantSummary);
        });

        return ConsultantListDTO.Response.builder()
                .prev(result.hasPrevious())
                .next(result.hasNext())
                .page(result.getNumber())
                .consultants(consultantSummaries)
                .build();
    }


    // [공통] 컨설턴트 상세 정보 조회
    @Override
    @Transactional(readOnly = true)
    public ConsultantInfoDTO.Response getConsultantDetail(Long memberSeq, String consultantNickname){


        // 존재하지 않는 컨설턴트이면 에러 반환
        ConsultantEntity consultant = consultantRepository.findByNickname(consultantNickname)
                .orElseThrow(() -> new ValidationException(ErrorCode.CONSULTANT_NOT_FOUND));

        List<ConsultantDetail> consultantDetails = new ArrayList<>();

        List<ConsultantEntity> result = consultantRepository.findConsultantDetail(consultantNickname);

        result.forEach(entity -> {
            List<ReviewEntity> reviewEntities = consultantRepository.findConsultantReviewsById(entity.getSeq());
            List<Review> reviews = reviewEntities.stream().map(e -> {
                LocalDateTime createdAt = e.getCreatedAt();
                LocalDateTime updatedAt = e.getUpdatedAt();
                LocalDateTime deletedAt = e.getDeletedAt();
                Long reviewSeq = e.getSeq();
                Float reviewGrade = e.getGrade();
                String reviewContent = e.getContent();
                Long reservationSeq = e.getReservation().getSeq();
                return Review.builder()
                        .reviewSeq(reviewSeq)
                        .reviewGrade(reviewGrade)
                        .reviewContent(reviewContent)
                        .createdAt(createdAt)
                        .updatedAt(updatedAt)
                        .deletedAt(deletedAt)
                        .reservationSeq(reservationSeq)
                        .build();
            }).collect(Collectors.toList());

            List<ScheduleEntity> scheduleEntities = consultantRepository.findConsultantSchedules(entity.getSeq());
            List<Schedule> schedules = scheduleEntities.stream().map(e-> {
                Long scheduleSeq = e.getSeq();
                LocalDateTime availableDateTime = e.getAvailableDateTime();
                Boolean isAvailable = e.getIsAvailable();
                return Schedule.builder()
                        .scheduleSeq(scheduleSeq)
                        .availableDateTime(availableDateTime)
                        .isAvailable(isAvailable)
                        .build();
            }).collect(Collectors.toList());

            ConsultantDetail consultantDetail = ConsultantDetail.builder()
                    .seq(entity.getSeq())
                    .nickname(entity.getMember().getNickname())
                    .profileUrl(entity.getMember().getProfileUrl())
                    .level(entity.getLevel())
                    .avgGrade(consultantRepository.avgGrade(entity.getNickname()))
                    .totalCnt(consultantRepository.totalCnt(entity.getNickname()))
                    .reviews(reviews)
                    .schedules(schedules)
                    .build();
            consultantDetails.add(consultantDetail);
        });
        return ConsultantInfoDTO.Response.builder()
                .consultant(consultantDetails)
                .build();
    }

    // [공통] 예약 내역 조회
    @Override
    @Transactional(readOnly = true)
    public UserReservationListDTO.Response getUserReservationsList(Long memberSeq) {

        List<UserReservationSummary> result = reservationRepository.findUserReservations(memberSeq);

        return UserReservationListDTO.Response.builder()
                .userReservationSummaries(result)
                .build();
    }

    // [컨설턴트] 예약 목록 조회
    @Override
    @Transactional(readOnly = true)
    public ConsultantReservationListDTO.Response getConsultantReservationsList(Long memberSeq, String consultantNickname, ConsultantReservationListDTO.Request dto) {

        memberSeq = dto.getMemberSeq();
        consultantNickname = dto.getConsultantNickname();

        Long checkSeq = consultantRepository.findConsultantMemberSeq(consultantNickname);

        if (!Objects.equals(checkSeq, memberSeq)){
            throw new ValidationException(ErrorCode.HANDLE_ACCESS_DENIED);
        }

        List<ConsultantReservationSummary> result = reservationRepository.findConsultantReservations(consultantNickname);

        return ConsultantReservationListDTO.Response.builder()
                .memberSeq(memberSeq)
                .consultantNickname(consultantNickname)
                .consultantReservationSummaries(result)
                .build();
    }

    // [컨설턴트] 예약 상세 조회
    @Override
    @Transactional(readOnly = true)
    public ConsultantReservationInfoDTO.Response getConsultantReservationDetail(Long memberSeq, String consultantNickname, Long reservationSeq, ConsultantReservationInfoDTO.Request dto ){

        memberSeq = dto.getMemberSeq();
        consultantNickname = dto.getConsultantNickname();
        reservationSeq = dto.getReservationSeq();


        // Reservation 이 존재하는지 확인
        ReservationEntity reservation = reservationRepository.findById(reservationSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESERVATION_NOT_FOUND));


        List<ConsultantReservationDetail> result = reservationRepository.findConsultantReservation(consultantNickname, reservationSeq);

        List<ConsultantReservationDetail> details = result.stream().map(entity -> {
            List<ImageEntity> imageEntities = reservationRepository.findReservationImages(entity.getReservationSeq());
            List<Image> images = imageEntities.stream().map(e->{
                Long imageSeq = e.getSeq();
                String imageUrl = e.getUrl();
                return Image.builder()
                        .imageSeq(imageSeq)
                        .imageUrl(imageUrl)
                        .build();
            }).collect(Collectors.toList());
            return ConsultantReservationDetail.builder()
                    .reservationSeq(entity.getReservationSeq())
                    .memberNickname(entity.getMemberNickname())
                    .reservationDateTime(entity.getReservationDateTime())
                    .reservationDetail(entity.getReservationDetail())
                    .images(images)
                    .build();
        }).collect(Collectors.toList());

        return ConsultantReservationInfoDTO.Response.builder()
                .memberSeq(memberSeq)
                .consultantNickname(consultantNickname)
                .reservationSeq(reservationSeq)
                .consultantReservationDetails(details)
                .build();

    }

    // [컨설턴트] 전체 후기, 평점 조회
    @Override
    @Transactional
    public ConsultantReviewListDTO.Response getConsultantReviewsList(Long memberSeq, String consultantNickname){
        List<ConsultantReviewSummary> result = consultantRepository.findConsultantReviewsByNickname(consultantNickname);
        return ConsultantReviewListDTO.Response.builder()
                .consultantReviewSummaries(result)
                .build();
    };

     //[컨설턴트] 평점 통계, 수익 조회
    @Override
    public ConsultantStatisticsDTO.Response getConsultantStatistics(Long memberSeq, String consultantNickname, ConsultantStatisticsDTO.Request dto){
        memberSeq = dto.getMemberSeq();
        consultantNickname = dto.getConsultantNickname();

        Long checkSeq = consultantRepository.findConsultantMemberSeq(consultantNickname);

        if (!Objects.equals(checkSeq, memberSeq)){
            throw new ValidationException(ErrorCode.HANDLE_ACCESS_DENIED);
        }


        return ConsultantStatisticsDTO.Response.builder()
                .avgGrade(consultantRepository.avgGrade(consultantNickname))
                .totalConsultingCnt(consultantRepository.totalCnt(consultantNickname))
                .totalUndeletedReviewCnt(consultantRepository.totalUndeletedReviewCnt(consultantNickname))
                .totalDeletedReviewCnt(consultantRepository.totalDeletedReviewCnt(consultantNickname))
                .totalSalary(consultantRepository.totalSalary(consultantNickname))
                .build();

    }

    // [공통] 리뷰 작성
    @Override
    @Transactional
    public ReviewSaveDTO.Response postReview(Long memberSeq, Long reservationSeq, ReviewSaveDTO.Request dto){

        memberSeq = dto.getMemberSeq();
        reservationSeq = dto.getReservationSeq();
        Float reviewGrade = dto.getReviewGrade();
        String reviewContent = dto.getReviewContent();

        if (memberSeq == null ||  reservationSeq == null || reviewGrade == null){
            throw new ValidationException(ErrorCode.MISSING_INPUT_VALUE);
        }

        ReservationEntity reservation = reservationRepository.findById(reservationSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.RESERVATION_NOT_FOUND));

        ReviewEntity review = ReviewEntity.builder()
                .grade(reviewGrade)
                .content(reviewContent)
                .reservation(reservation)
                .build();

        ReviewEntity save = reviewRepository.save(review);

        return ReviewSaveDTO.Response.builder()
                .success(true)
                .seq(save.getSeq())
                .build();
    }

    @Override
    @Transactional
    public ReviewUpdateDTO.Response updateReview(Long memberSeq, Long reviewSeq, ReviewUpdateDTO.Request dto ) {

        memberSeq = dto.getMemberSeq();
        reviewSeq = dto.getReviewSeq();
        Float reviewGrade = dto.getReviewGrade();
        String reviewContent = dto.getReviewContent();

        // 입력값 검증
        if (memberSeq == null || reviewSeq == null || reviewGrade == null) {
            throw new ValidationException(ErrorCode.MISSING_INPUT_VALUE);
        }

        // 회원 검증
        MemberEntity member = memberRepository.findById(memberSeq)
                .orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND));

        // 존재하는 리뷰인지 확인
        ReviewEntity review = reviewRepository.findById(reviewSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.REVIEW_NOT_FOUND));

        // 내가 작성한 리뷰가 아닌 리뷰를 수정하면 error
        if (review.getReservation().getMember().getSeq() != member.getSeq()) {
            throw new ValidationException(ErrorCode.HANDLE_ACCESS_DENIED);
        }

        review.updateContent(reviewContent);
        return ReviewUpdateDTO.Response.builder()
                .success(true)
                .build();
        }

    @Override
    @Transactional
    public ReviewDeleteDTO.Response deleteReview(Long memberSeq, Long reviewSeq, ReviewDeleteDTO.Request dto) {
        memberSeq = dto.getMemberSeq();
        reviewSeq = dto.getReviewSeq();
        Float reviewGrade = dto.getReviewGrade();
        String reviewContent = dto.getReviewContent();

        // 입력값 검증
        if (memberSeq == null || reviewSeq == null || reviewGrade == null) {
            throw new ValidationException(ErrorCode.MISSING_INPUT_VALUE);
        }

        // 회원 검증
        MemberEntity member = memberRepository.findById(memberSeq)
                .orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND));

        // 존재하는 리뷰인지 확인
        ReviewEntity review = reviewRepository.findById(reviewSeq)
                .orElseThrow(() -> new NotFoundException(ErrorCode.REVIEW_NOT_FOUND));

        // 내가 작성한 리뷰가 아닌 리뷰를 수정하면 error
        if (review.getReservation().getMember().getSeq() != member.getSeq()) {
            throw new ValidationException(ErrorCode.HANDLE_ACCESS_DENIED);
        }

        reviewRepository.delete(review);
        return ReviewDeleteDTO.Response.builder()
                .success(true)
                .build();

    }

    // [공통] 에약 상세 조회
    @Transactional
    public UserReservationInfoDTO.Response getUserReservationDetail(Long memberSeq, Long reservationSeq, UserReservationInfoDTO.Request dto) {

        memberSeq = dto.getMemberSeq();
        reservationSeq = dto.getReservationSeq();

        ReservationEntity reservation = reservationRepository.findById(reservationSeq)
                .orElseThrow(() -> new ValidationException(ErrorCode.RESERVATION_NOT_FOUND));


        List<UserReservationDetail> result = reservationRepository.findUserReservation(memberSeq, reservationSeq);

        List<UserReservationDetail> details = result.stream().map(entity -> {
            List<ImageEntity> imageEntities = reservationRepository.findReservationImages(entity.getReservationSeq());

            List<Image> images = imageEntities.stream().map(e->{
                Long imageSeq = e.getSeq();
                String imageUrl = e.getUrl();
                return Image.builder()
                        .imageSeq(imageSeq)
                        .imageUrl(imageUrl)
                        .build();
            }).collect(Collectors.toList());

            return UserReservationDetail.builder()
                    .reservationSeq(entity.getReservationSeq())
                    .consultantNickname(entity.getConsultantNickname())
                    .reservationDateTime(entity.getReservationDateTime())
                    .reservationDetail(entity.getReservationDetail())
                    .images(images)
                    .build();
        }).collect(Collectors.toList());

        return UserReservationInfoDTO.Response.builder()
                .memberSeq(memberSeq)
                .reservationSeq(reservationSeq)
                .userReservationDetails(details)
                .build();
    }

    @Override
    @Transactional
    public ScheduleSaveDTO.Response saveSchedule(ScheduleSaveDTO.Request dto) {
        Long memberSeq = dto.getMemberSeq();

        //입력값 검증
        if (memberSeq == null) {
            throw new ValidationException(ErrorCode.MISSING_INPUT_VALUE);
        }

        //멤버 존재하는지 확인
        MemberEntity member = memberRepository.findById(memberSeq)
                .orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND));

        //컨설턴트 인지 확인
        ConsultantEntity consultant = consultantRepository.findByNickname(member.getNickname())
                .orElseThrow(() -> new ValidationException(ErrorCode.CONSULTANT_NOT_FOUND));

        // schedule entity 만들기
        List<ScheduleEntity> scheduleEntities = new ArrayList<>();
        for(int i=0; i<dto.getAvailableDateTimes().size(); i++){
            ScheduleEntity entity = ScheduleEntity.builder()
                    .availableDateTime(dto.getAvailableDateTimes().get(i))
                    .consultant(consultant)
                    .build();


            scheduleEntities.add(entity);
        }
        scheduleRepository.saveAll(scheduleEntities);

        return ScheduleSaveDTO.Response.builder()
                .success(true)
                .build();

    }

    @Override
    @Transactional
    public ScheduleDeleteDTO.Response deleteSchedule(ScheduleDeleteDTO.Request dto, Long scheduleSeq) {
        Long memberSeq = dto.getMemberSeq();

        //입력값 검증
        if (memberSeq == null || scheduleSeq == null) {
            throw new ValidationException(ErrorCode.MISSING_INPUT_VALUE);
        }

        //멤버 존재하는지 확인
        MemberEntity member = memberRepository.findById(memberSeq)
                .orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND));

        ScheduleEntity scheduleEntity = scheduleRepository.findById(scheduleSeq)
                .orElseThrow(() ->new NotFoundException(ErrorCode.SCHEDULE_NOT_FOUND));

        //자신의 스케쥴이 아니거나 권한이 존재하지 않으면 삭제 불가
        if (member.getSeq() != scheduleEntity.getConsultant().getMember().getSeq()
                && !member.getMemberRoles().contains(MemberRole.ADMIN)) {
            throw new AccessDeniedException(ErrorCode.HANDLE_ACCESS_DENIED);
        }


        //삭제
        scheduleRepository.delete(scheduleEntity);

        return ScheduleDeleteDTO.Response.builder()
                .success(true)
                .build();
    }

    @Override
    @Transactional
    public ConsultantReservationSaveDTO.Response saveReservation(ConsultantReservationSaveDTO.Request dto) {

        /**
         * 1. 유저가 비어있는 컨설턴트 스케줄에 맞춰 예약을 한다
         * 2. 예약을 함과 동시에 사진을 넣는다.
         *
         * 이 순서 맞을까..? (맞다면 isConsultant 빼고해도 될듯?)
         * 추가로 컨설턴트 이미지, 유저 이미지를 분리해야할거 같아 ReservationEntity에서 이미지를 2개로 나누었다.
         */

        Long memberSeq = dto.getMemberSeq();

        // 입력값 검증
        // 퍼스널 컬러는 없을 수도 있으니까 제외!
        if (memberSeq == null || dto.getConsultantNickname() == null || dto.getAge() == null ||
        dto.getGender() == null || dto.getHeight() == null || dto.getWeight() == null) {
            throw new ValidationException(ErrorCode.MISSING_INPUT_VALUE);
        }

        // 멤버 존재하는지 확인
        MemberEntity member = memberRepository.findById(memberSeq)
                .orElseThrow(() -> new NotFoundException(MEMBER_NOT_FOUND));

        // 컨설턴트 존재하는지 확인
        ConsultantEntity consultant = consultantRepository.findByNickname(dto.getConsultantNickname())
                .orElseThrow(() -> new ValidationException(ErrorCode.CONSULTANT_NOT_FOUND));

        // 유저가 입력한 정보 기반으로 업데이트 진행
        member.updateReservationInfo(dto);

        boolean isConsultant = false;
        for (MemberRoleEntity role : member.getMemberRoles()) {
            if (role.getMemberRole().equals(MemberRole.CONSULTANT))
                isConsultant = true;
        }

        // 사진이랑 세부정보는 예약에 저장
        // 예약 save 하는 로직
        ReservationEntity.ReservationEntityBuilder reservationEntityBuilder = ReservationEntity.builder()
                .schedule(null) // TODO: 컨설턴트의 어느 스케줄을 썼는지 어떻게 보내줄까
                .member(member)
                .date(dto.getAvailableDateTime())
                .detail(dto.getDetail())
                .roomNumber(HashUtil.makeRoomNumber());

        if (isConsultant) {
            // TODO: consultantImages에 사진을 넣어라
        } else {
            // TODO: memberImages에 사진을 넣어라
        }
        return null;
    }

    @Override
    public UserReservationInfoDTO.ReservationEnterResponse getReservationEnterInfo(Long memberSeq, Long reservationSeq) {

        memberRepository.findById(memberSeq).orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));
        ReservationEntity reservation = reservationRepository.findById(reservationSeq).orElseThrow(() -> new NotFoundException(RESERVATION_NOT_FOUND));

        List<String> consultantImages = reservation.getConsultantImages().stream().map(obj -> obj.getUrl()).collect(Collectors.toList());
        List<String> memberImages = reservation.getMemberImages().stream().map(obj -> obj.getUrl()).collect(Collectors.toList());

        return UserReservationInfoDTO.ReservationEnterResponse.builder()
                .roomNumber(reservation.getRoomNumber())
                .consultantNickname(reservation.getSchedule().getConsultant().getNickname()) // 와... 망해따..
                .memberNickname(reservation.getMember().getNickname())
                .consultantImages(consultantImages)
                .memberImages(memberImages)
                .build();
    }

    @Override
    @Transactional
    public ConsultantReservationSaveDTO.Response saveConsultantImages(ConsultantReservationSaveDTO.ConsultantImageSaveRequest dto) {

        ReservationEntity reservationEntity = reservationRepository.findById(dto.getReservationSeq()).orElseThrow(() -> new NotFoundException(RESERVATION_NOT_FOUND));
        ImageSaveDTO.Response savedImage = saveImage(dto.getImages());

        List<ImageDTO> imageInfos = savedImage.getImageInfos();
        List<ImageEntity> imageEntities  = new ArrayList<>();
        for (ImageDTO image : imageInfos) {
            imageEntities.add(ImageEntity.builder()
                    .reservation(reservationEntity)
                    .url(image.getFileUrl())
                    .build());
        }

        reservationEntity.setConsultantImages(imageEntities);
        return ConsultantReservationSaveDTO.Response.builder()
                .reservationSeq(reservationEntity.getSeq())
                .success(true)
                .build();
    }

    private ImageSaveDTO.Response saveImage(List<MultipartFile> images) {
        return imageService.save(ImageSaveDTO.Request.builder()
                .images(images)
                .build());
    }
}












