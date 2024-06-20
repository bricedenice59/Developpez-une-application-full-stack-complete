import {ICreatedAt} from "../created-at";

export interface IPost extends ICreatedAt {
    id : number,
    topicId: number,
    topicName: string,
    title: string
    description: string
    author: string
}
