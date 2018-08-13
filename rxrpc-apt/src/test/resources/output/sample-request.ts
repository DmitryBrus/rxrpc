import { SampleData } from './';

export interface SampleRequest {
    id: number;
    name: string;
    data: SampleData;
    mapData: Map<string, any>;
    multipleData: SampleData[];
}
