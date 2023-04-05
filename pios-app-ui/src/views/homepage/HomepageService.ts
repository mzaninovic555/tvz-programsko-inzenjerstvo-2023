import api from '../../common/api';
import ComponentResponse from '~/views/component-search/ComponentResponse';
import Post from '~/views/forum/Post';

export async function getTopRatedBuilds(): Promise<ComponentResponse[]> {
  const response = await api.get<ComponentResponse[]>(`/v1/component/top-rated`);
  return response.data;
}

export async function getLatestPosts(count: number): Promise<Post[]> {
  const response = await api.get<Post[]>(`/v1/forum/latest?count=${count}`);
  return response.data;
}
