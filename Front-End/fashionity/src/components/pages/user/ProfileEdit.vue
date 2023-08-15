<template>
  <div class="container-fluid">
    <the-nav-bar-mypage></the-nav-bar-mypage>
    <div class="block" style="height: 3rem"></div>
    <div class="container" style="flex-direction: column">
      <div class="edit-info row" style="justify-content: center" align="left">
        <div class="row">
          <form v-on:submit.prevent="editProfile">
            <div>
              <div
                class="profileImg-buttons"
                style="display: flex; justify-content: center; flex-direction: row"
              >
                <div class="profile-main-photo" v-if="!files.length">
                  <img
                    v-if = "selectedImage === null"
                    class="image-box"
                    width="200"
                    height="200"
                    :src="profileUrl"
                    alt=""
                    ref = "profileImage"
                  />
                </div>
                <div v-else class="file-preview-content-container">
                  <div class="file-preview-container">
                    <div v-for="(file, index) in files" :key="index" class="file-preview-wrapper">
                      <div class="file-close-button" @click="fileDeleteButton" :name="file.number">x</div>
                      <img :src="file.preview" />
                    </div>
                  </div>
                </div>
                <div v-if="cropImgURL" class="row justify-content-start">
                  <div class="col">
                    <cropper
                      class="cropper"
                      ref="cropper"
                      :auto-zoom="true"
                      :stencil-size="{
                        width: 280,
                        height: 280,
                      }"
                      image-restriction="stencil"
                      :stencil-props="{
                        aspectRatio: 1 / 1,
                      }"
                      :src="cropImgURL"
                    />
                  </div>
                  <div class="col" @click="uploadImage">upload</div>
                </div>
                <div class="buttons" style="display: flex; align-items: flex-end">
                  <div class = "filebox">
                    <!-- <button class="active-button" style="margin-right: 0.3rem" @click = "showImageUpload"> -->
                      <label for="file">사진 변경</label>
                      <input type="file" id = "upload" ref = "uploadInput" @change="imageUpload"/>
                    <!-- </button> -->
                  </div>
                  
                  <button class="inactive-button" style="margin-right: 0.3rem">
                    사진 삭제
                  </button>
                  <!-- <button class="change-password-button" style="margin-right: 0.3rem">
                    비밀번호 변경
                  </button> -->
                </div>
              </div>
            </div>
            <div style = "height:5rem"></div>
            <h5><b>아이디</b></h5>
            <div>{{ id }}</div>
            <hr />
            <h5><b>이메일</b></h5>
            <div>{{ email }}</div>
            <hr />
            <div>
              <h5>
                <label for="nickname"><b>닉네임</b></label>
              </h5>
              <input id="nickname" type="text" v-model="nickname" />
              <hr />
            </div>
            <div>
              <h5>
                <label for="profileIntro"><b>한줄 소개</b></label>
              </h5>
              <input
                id="profileIntro"
                type="text"
                v-model="profileIntro"
                style="width: 500px"
              />
              <hr />
            </div><br>
            <div style = "display:flex; justify-content: flex-end;">
              <button
                class="active-button"
                style="margin-right: 0.3rem"
              ><input type = "submit" value = "수정하기">
              </button>
              <button class="delete-button" @click="delteProfileAndLogout">탈퇴하기</button>
            </div>
          </form>
        </div>
      </div>
    </div>


  </div>
  <div class=" " style="height: 2rem"></div>
</template>
<script>
import { mapState, mapGetters, mapActions } from "vuex";
// import FormData from "form-data";
import { Cropper } from "vue-advanced-cropper";
import "vue-advanced-cropper/dist/style.css";
import TheNavBarMypage from "@/components/layout/TheNavBarMypage.vue";
import axios from "axios";
import memberStore from "@/store/modules/memberStore";

let token = sessionStorage.getItem("token");

export default {
  name: "ProfileEdit",
  components: {
    TheNavBarMypage,
    Cropper
  },
  data() {
    return {
      profileUrl: "",
      id: "",
      nickname: "",
      email: "",
      profileIntro: "",
      selectedImage:null,
      displayProfileImageUpload: false,
      files: [], //업로드용 파일
      filesPreview: [],
      uploadImageIndex: 0, // 이미지 업로드를 위한 변수
      img: "https://images.pexels.com/photos/4323307/pexels-photo-4323307.jpeg",
      cropImgURL: "",
      currImgList: [],
      currFileList: [],
    };
  },
  created() {
    this.getProfile();
  },
  computed: {
    ...mapState(memberStore, ["isLogin", "loginUser"]),
    ...mapGetters(["checkUserInfo"]),
    // selectedImagePreview(){
    //   if (this.selectedImage){
    //     return URL.createObjectURL(this.selectedImage)
    //   }
    //   return "";
    // }
  },
  methods: {
    ...mapActions(memberStore, ["logoutAction"]),

    logout() {
      this.logoutAction;
    },
    getProfile() {
      axios({
        method: "get",
        url: `${process.env.VUE_APP_API_URL}/api/v1/members`,
        headers: { Authorization: `Bearer ${token}` },
      }).then(({ data }) => {
        this.nickname = data.nickname;
        this.profileUrl = data.profileUrl;
        this.id = data.id;
        this.email = data.email;
        this.profileIntro = data.profileIntro;
        console.log(data);
      });
    },
    async deleteProfile() {
      alert("계정이 삭제됩니다.");
      axios({
        method: "put",
        url: `${process.env.VUE_APP_API_URL}/api/v1/members/delete`,
        headers: { Authorization: `Bearer ${token}` },
      })
      .then((data) => console.log(data.data.success));
    },
    async delteProfileAndLogout(){
      await this.deleteProfile();
      await this.logout();
      this.$router.push("/")
    },
    makePreview(blobData) {
      for (let i = 0; i < this.$refs.files.files.length; i++) {
        console.log("안녕 난 for문이야");
        this.files = [
          ...this.files,
          //이미지 업로드
          {
            //실제 파일
            // file: this.$refs.files.files[i],
            file: this.cropImgURL,
            //이미지 프리뷰
            // preview: URL.createObjectURL(this.$refs.files.files[i]),
            preview: this.cropImgURL,
            //삭제및 관리를 위한 number
            number: this.uploadImageIndex,
          },
        ];
        // num = i;
        //이미지 업로드용 프리뷰
        this.filesPreview = [
          ...this.filesPreview,
          {
            file: URL.createObjectURL(this.$refs.files.files[i]),
            number: this.uploadImageIndex,
            binaryFile: blobData,
          },
        ];
      }
      this.uploadImageIndex++; //이미지 index의 마지막 값 + 1 저장

      // this.uploadImageIndex += 1;
      console.log(this.files);

      console.log("프리뷰 입니당", this.filesPreview);
      // console.log(this.filesPreview);
      this.cropImgURL = "";
      this.currImgList = this.filesPreview.map((row) => row.file);
      this.currFileList = this.filesPreview.map((row) => row.binaryFile);
    },
    imageUpload() {
      console.log("upload");
      console.log(this.$refs.files.files);

      console.log(this.$refs.files.files[this.$refs.files.files.length - 1]);
      this.cropImgURL = URL.createObjectURL(
        this.$refs.files.files[this.$refs.files.files.length - 1]
      );
      console.log("url입니당", this.cropImgURL);
    },

    fileDeleteButton(e) {
      const name = e.target.getAttribute("name");
      this.files = this.files.filter((data) => data.number !== Number(name));
      this.filesPreview = this.filesPreview.filter((data) => data.number !== Number(name));
      // console.log(this.files);
      this.currImgList = this.filesPreview.map((row) => row.file);
      this.currFileList = this.filesPreview.map((row) => row.binaryFile);
    },
    uploadImage() {
      const { canvas } = this.$refs.cropper.getResult();
      console.log(canvas);
      if (canvas) {
        // const form = new FormData();
        // canvas.toBlob((blob) => {
        //   form.append("file", blob);
        //   // You can use axios, superagent and other libraries instead here
        //   // fetch("http://example.com/upload/", {
        //   //   method: "POST",
        //   //   body: form,
        //   // });
        //   // Perhaps you should add the setting appropriate file format here
        // }, "image/jpeg");
        // const url = window.URL.createObjectURL(form);
        var blobData = "";
        canvas.toBlob((blob) => {
          console.log("blob", blob);
          this.cropImgURL = canvas.toDataURL();

          this.makePreview(blob);
          blobData = blob;
        });

        console.log("blob 후", canvas.toDataURL());
        console.log(blobData);
        // let blob = new Blob([new ArrayBuffer(canvas.toDataURL())], {
        //   type: "image/png",
        // });
        // const url = window.URL.createObjectURL(blob); // blob:http://localhost:1234/28ff8746-94eb-4dbe-9d6c-2443b581dd30

        // this.cropImgURL = canvas.toDataURL();

        // this.makePreview(blobData);
      }
    },
  },
  change({ coordinates, canvas }) {
    console.log(coordinates, canvas);
  },
  watch: {
    cropImgURL(newVal) {
      this.cropImgURL = newVal;
      console.log("watch", this.cropImgURL);
    },
    currFileList(newVal) {
      this.currFileList = newVal;
      console.log("newval", newVal);
      this.$emit("updateImg", this.currFileList);
    },
  },
    // async fetchImageAsFile(profileUrl) {
    //   if(profileUrl !== null){
    //     const response = await fetch(profileUrl);
    //     const data = await response.blob();
    //     const ext = profileUrl.split("/").pop();
    //     const filename = profileUrl.split("/").pop()
    //     const metadata = {type:`image/${ext}`}
    //     return new File([data],filename, metadata);
        
    //   }
    // },
    // async editProfile() {
    //   // const updatedProfile = {
    //   //   profileImage: this.selectedImage,
    //   //   nickname: this.nickname,
    //   //   profileIntro: this.profileIntro,
    //   // };
    //   const formData = new FormData();
    //   if (this.selectedImage !== null) {
    //     formData.append("profileImage", this.selectedImage, this.selectedImage.name);
    //   }
    //   else { 
    //     const file = await this.fetchImageAsFile(this.profileUrl)
    //     formData.append("profileImage", file)}
    //   // formData.append("profileImage", this.selectedImage);
    //   formData.append("nickname", this.nickname);
    //   formData.append("profileIntro", this.profileIntro);
    //   console.log(this.selectedImage)
    //   console.log(this.nickname)
    //   console.log(this.profileIntro)
      
    //   for (let key of formData.keys()){
    //     console.log(key)
    //   }
    //   for (let value of formData.values()){
    //     console.log(value)
    //   }

    //   axios({
    //     method: "post",
    //     url: `${process.env.VUE_APP_API_URL}/api/v1/members/edit`,
    //     data: formData,
    //     headers: { Authorization: `Bearer ${token}`, "Content-Type": "multipart/form-data"},
    //   })
    //     .then((data) => {
    //       console.log("=======성공성공========");
    //       console.log(data);
    //       console.log("Profile updated successfully!");
    //     })
    //     .catch((error) => {
    //       // console.log(formData)
    //       console.log("=======에러에러========");
    //       console.log(error);
    //     });
    // },

    // handleImageChange(event) {
    //   this.selectedImage = event.target.files[0];

    //   if (this.selectedImage) {
    //     const reader = new FileReader();

    //     reader.onload = (event) => {
    //       this.profileUrl = event.target.result;
    //     };

    //     reader.readAsDataURL(this.selectedImage);
    //   }

    //   // Clear the input value to allow selecting the same image again
    //   this.$refs.uploadInput.value = "";
    // },

};
</script>
<style>
.image-box {
  border-radius: 50%;
}
.profile-main-photo {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 300px;
  height: 200px;
  object-fit: block;
}
.active-button {
  width: 100px;
  height: 40px;
  flex-shrink: 0;
  border-radius: 10px;
  background: #2191ff;
  color: #ffffff;
}
.inactive-button {
  width: 100px;
  height: 40px;
  flex-shrink: 0;
  border-radius: 10px;
  background: #cecece;
  color: #ffffff;
}
.change-password-button {
  width: 120px;
  height: 40px;
  flex-shrink: 0;
  border-radius: 10px;
  background: #cecece;
  color: #ffffff;
}
.delete-button {
  width: 100px;
  height: 40px;
  flex-shrink: 0;
  border-radius: 10px;
  background: #ed4141;
  color: #ffffff;
}
.filebox label {
  width: 100px;
  height: 40px;
  flex-shrink: 0;
  border-radius: 10px;
  background: #2191ff;
  color: #ffffff;
  margin-right: 0.3rem;
  text-align:center;
  line-height: 40px;
}
.filebox input[type="file"]{
  width: 0;
  height: 0;
  opacity: 0;
}
.cropper {
  max-height: 300px;
  max-width: 300px;
  background: #ddd;
}
</style>
