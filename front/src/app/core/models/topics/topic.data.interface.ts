import {ICreatedAt} from "../created-at";

export interface ITopicData extends ICreatedAt {
    id : number
    title: string
    description: string
    isSubscribed: boolean
}
