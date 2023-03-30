import api from '../../../common/api';
import ReviewResponse from '~/views/component-search/reviews/ReviewResponse';
import ReviewRequest from '~/views/component-search/reviews/ReviewRequest';

export async function createReview(req: ReviewRequest): Promise<ReviewResponse> {
  const response = await api.post<ReviewResponse>(`/v1/review/add`, req);
  return response.data;
}

export async function removeReview(componentId: number): Promise<ReviewResponse> {
  const response = await api.post<ReviewResponse>(`/v1/review/remove/${componentId}`);
  return response.data;
}
