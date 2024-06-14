import {ICreatedAt} from "../../../core/interfaces/response.date";

export interface ITopicData extends ICreatedAt {
    id : number
    title: string
    description: string
    isSubscribed: boolean
}
