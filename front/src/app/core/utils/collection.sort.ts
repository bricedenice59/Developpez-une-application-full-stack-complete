import {ICreatedAt} from "../models/created-at";

export class CollectionSort{
  public static sortByCreationDateAscending<T extends ICreatedAt>(posts: T[]): T[] {
    return posts.sort((a, b) => {
      if (a.createdAt !== undefined && a.createdAt !== null && a.createdAt.trim() !== "") {
        return new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime();
      }
      return 0;
    });
  }

  public static sortByCreationDateDescending<T extends ICreatedAt>(posts: T[]): T[] {
    return posts.sort((a, b) => {
      if (a.createdAt !== undefined && a.createdAt !== null && a.createdAt.trim() !== "") {
        return new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime();
      }
      return 0;
    });
  }
}
