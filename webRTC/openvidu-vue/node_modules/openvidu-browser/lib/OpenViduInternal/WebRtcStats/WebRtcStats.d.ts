import { Stream } from '../../OpenVidu/Stream';
/**
 * Common WebRtcSTats for latest Chromium and Firefox versions
 */
interface IWebrtcStats {
    inbound?: {
        audio: {
            bytesReceived: number;
            packetsReceived: number;
            packetsLost: number;
            jitter: number;
        } | {};
        video: {
            bytesReceived: number;
            packetsReceived: number;
            packetsLost: number;
            jitter?: number;
            jitterBufferDelay?: number;
            framesDecoded: number;
            firCount: number;
            nackCount: number;
            pliCount: number;
            frameHeight?: number;
            frameWidth?: number;
            framesDropped?: number;
            framesReceived?: number;
        } | {};
    };
    outbound?: {
        audio: {
            bytesSent: number;
            packetsSent: number;
        } | {};
        video: {
            bytesSent: number;
            packetsSent: number;
            firCount: number;
            framesEncoded: number;
            nackCount: number;
            pliCount: number;
            qpSum: number;
            frameHeight?: number;
            frameWidth?: number;
            framesSent?: number;
        } | {};
    };
    candidatepair?: {
        currentRoundTripTime?: number;
        availableOutgoingBitrate?: number;
    };
}
export declare class WebRtcStats {
    private stream;
    private readonly STATS_ITEM_NAME;
    private webRtcStatsEnabled;
    private webRtcStatsIntervalId;
    private statsInterval;
    private POST_URL;
    constructor(stream: Stream);
    isEnabled(): boolean;
    initWebRtcStats(): void;
    getSelectedIceCandidateInfo(): Promise<any>;
    stopWebRtcStats(): void;
    private sendStats;
    private sendStatsToHttpEndpoint;
    getCommonStats(): Promise<IWebrtcStats>;
    private generateJSONStatsResponse;
    private getWebRtcStatsResponseOutline;
}
export {};
