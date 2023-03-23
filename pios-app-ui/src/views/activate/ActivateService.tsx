import ActivateResponse from "../../views/activate/ActivateResponse";
import api from "../../common/api";
import {AxiosError} from "axios";

export async function activate(activationToken: string) : Promise<ActivateResponse> {
  const response = await api.post<ActivateResponse>('/v1/activate', {activationToken: activationToken})
                            .catch((e: AxiosError) => e.response);
  return response?.data ?? {}
}