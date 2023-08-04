export interface CustomMediaStreamConstraints {
    constraints: MediaStreamConstraints;
    audioTrack: MediaStreamTrack | undefined;
    videoTrack: MediaStreamTrack | undefined;
}
