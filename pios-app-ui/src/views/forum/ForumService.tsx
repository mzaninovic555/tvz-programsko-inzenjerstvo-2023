import api from '../../common/api';
import Post from '../../views/forum/Post';
import ForumResponse from '../../views/forum/ForumResponse';


export async function getForumPosts(search: string): Promise<Post[]> {
  const response = await api.get<Post[]>(`/v1/forum?title=${search}`);
  return response.data;
}

export async function getForumPostById(id: string | undefined): Promise<ForumResponse> {
  const response = await api.get<ForumResponse>(`/v1/forum/${id}`);
  return response.data;
}
