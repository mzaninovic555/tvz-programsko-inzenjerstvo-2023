import api from '../../../common/api';
import BuildCreateResponse from '~/views/builds/service/BuildCreateResponse';
import BuildResponse from '~/views/builds/service/BuildResponse';
import BasicResponse from '~/common/messages/BasicResponse';
import BuildInfoChangeRequest from '~/views/builds/service/BuildInfoChangeRequest';
import BuildChangeResponse from '~/views/builds/service/BuildChangeResponse';
import BuildComponentChangeRequest from '~/views/builds/service/BuildComponentChangeRequest';

export async function createBuild(): Promise<BuildCreateResponse> {
  const response = await api.post<BuildCreateResponse>(`/v1/build/create`);
  return response.data;
}

export async function getUserBuilds(): Promise<BuildResponse[]> {
  const response = await api.get<BuildResponse[]>('/v1/build/my-builds');
  return response.data;
}

export async function getBuildInfo(link: string): Promise<BuildResponse> {
  const response = await api.get<BuildResponse>(`/v1/build/from-link/${link}`);
  return response.data;
}

export async function deleteBuild(link: string): Promise<BasicResponse> {
  const response = await api.post<BasicResponse>(`/v1/build/delete/${link}`);
  return response.data;
}

export async function changeBuildInfo(build: BuildInfoChangeRequest): Promise<BuildChangeResponse> {
  const response = await api.post<BuildChangeResponse>(`/v1/build/edit/info`, build);
  return response.data;
}

export async function changeBuildComponent(component: BuildComponentChangeRequest): Promise<BuildChangeResponse> {
  const response = await api.post<BuildChangeResponse>(`/v1/build/edit/component`, component);
  return response.data;
}
