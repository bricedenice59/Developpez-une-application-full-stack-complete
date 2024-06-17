import {ICreatedAt} from "../../../core/interfaces/response.date";

export interface ITopicResponse extends ICreatedAt {
    id : number,
    title: string
    description: string
}
