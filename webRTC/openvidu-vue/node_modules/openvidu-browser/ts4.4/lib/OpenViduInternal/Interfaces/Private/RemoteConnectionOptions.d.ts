import { StreamOptionsServer } from './StreamOptionsServer';
export interface RemoteConnectionOptions {
    id: string;
    createdAt: number;
    metadata: string;
    streams: StreamOptionsServer[];
}
