import {ICreatedAt} from "../created-at";

export interface ITopic extends ICreatedAt {
    id : number,
    title: string
    description: string
}
