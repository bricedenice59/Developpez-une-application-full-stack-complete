import {ICreatedAt} from "../../../core/interfaces/response.date";

export interface IPostResponse extends ICreatedAt {
    id : number,
    topicId: number,
    topicName: string,
    title: string
    description: string
    author: string
}
