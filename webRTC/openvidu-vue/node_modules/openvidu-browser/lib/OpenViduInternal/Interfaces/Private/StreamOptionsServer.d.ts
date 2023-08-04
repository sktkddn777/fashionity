import { Filter } from '../../../OpenVidu/Filter';
import { TypeOfVideo } from '../../Enums/TypeOfVideo';
export interface StreamOptionsServer {
    id: string;
    createdAt: number;
    hasAudio: boolean;
    hasVideo: boolean;
    audioActive: boolean;
    videoActive: boolean;
    typeOfVideo: TypeOfVideo;
    frameRate: number;
    videoDimensions: string;
    filter: Filter;
}
