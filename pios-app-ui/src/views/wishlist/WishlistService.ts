import WishlistEntry from '~/views/wishlist/WishlistEntry';
import api from '../../common/api';
import BasicResponse from '~/common/messages/BasicResponse';

export async function getUserWishlist(): Promise<WishlistEntry[]> {
  const response = await api.get<WishlistEntry[]>('/v1/wishlist');
  return response.data;
}

export async function addComponentToWishlist(componentId: number): Promise<BasicResponse> {
  const response = await api.post<BasicResponse>(`/v1/wishlist/add`, {componentId});
  return response.data;
}

export async function clearUserWishlist(): Promise<BasicResponse> {
  const response = await api.post<BasicResponse>(`/v1/wishlist/clear`);
  return response.data;
}

export async function deleteItemFromWishlist(id: number): Promise<BasicResponse> {
  const response = await api.delete<BasicResponse>(`/v1/wishlist/${id}`);
  return response.data;
}
