import { Connection } from '../../../OpenVidu/Connection';
import { Filter } from '../../../OpenVidu/Filter';
import { TypeOfVideo } from '../../Enums/TypeOfVideo';
export interface InboundStreamOptions {
    id: string;
    createdAt: number;
    connection: Connection;
    hasAudio: boolean;
    hasVideo: boolean;
    audioActive: boolean;
    videoActive: boolean;
    typeOfVideo: TypeOfVideo;
    frameRate: number;
    videoDimensions: {
        width: number;
        height: number;
    };
    filter?: Filter;
}
