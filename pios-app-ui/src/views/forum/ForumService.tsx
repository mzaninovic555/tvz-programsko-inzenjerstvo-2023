import api from '../../common/api';
import Post from '../../views/forum/Post';
import ForumResponse from '../../views/forum/ForumResponse';
import ForumPostCreateResponse from '../../views/forum/ForumEntryResponse';
import ForumPostCreateRequest from '~/views/forum/ForumPostCreateRequest';
import BasicResponse from '~/common/messages/BasicResponse';


export async function getForumPosts(search: string): Promise<Post[]> {
  const response = await api.get<Post[]>(`/v1/forum?title=${search}`);
  return response.data;
}

export async function getForumPostById(id: string | undefined): Promise<ForumResponse> {
  const response = await api.get<ForumResponse>(`/v1/forum/id/${id}`);
  return response.data;
}

export async function createForumPost(request: ForumPostCreateRequest): Promise<ForumPostCreateResponse> {
  const response = await api.post<ForumPostCreateResponse>(`/v1/forum/create`, request);
  return response.data;
}

export async function deleteForumPost(id: number): Promise<BasicResponse> {
  const response = await api.get<BasicResponse>(`/v1/forum/delete/${id}`);
  return response.data;
}
