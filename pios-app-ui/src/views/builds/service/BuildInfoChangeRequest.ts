interface BuildInfoChangeRequest {
  link: string;
  title?: string;
  description?: string;
  isFinalized: boolean;
  isPublic: boolean;
}

export default BuildInfoChangeRequest;
