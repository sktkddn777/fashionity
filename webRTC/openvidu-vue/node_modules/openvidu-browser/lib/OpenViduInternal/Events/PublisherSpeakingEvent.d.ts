import { Event } from './Event';
import { Connection } from '../../OpenVidu/Connection';
import { Session } from '../../OpenVidu/Session';
import { StreamManager } from '../../OpenVidu/StreamManager';
/**
 * Triggered by:
 * - `publisherStartSpeaking` (available for [Session](/en/stable/api/openvidu-browser/interfaces/SessionEventMap.html#publisherStartSpeaking) and [StreamManager](/en/stable/api/openvidu-browser/interfaces/StreamManagerEventMap.html#publisherStartSpeaking) objects)
 * - `publisherStopSpeaking` (available for [Session](/en/stable/api/openvidu-browser/interfaces/SessionEventMap.html#publisherStopSpeaking) and [StreamManager](/en/stable/api/openvidu-browser/interfaces/StreamManagerEventMap.html#publisherStopSpeaking) objects)
 */
export declare class PublisherSpeakingEvent extends Event {
    /**
     * The client that started or stopped speaking
     */
    connection: Connection;
    /**
     * The streamId of the Stream affected by the speaking event
     */
    streamId: string;
    /**
     * @hidden
     */
    constructor(target: Session | StreamManager, type: string, connection: Connection, streamId: string);
    /**
     * @hidden
     */
    callDefaultBehavior(): void;
}
