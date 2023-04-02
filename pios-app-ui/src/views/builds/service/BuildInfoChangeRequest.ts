interface BuildInfoChangeRequest {
  link: string;
  title?: string;
  description?: string;
  finalized: boolean;
  isPublic: boolean;
}

export default BuildInfoChangeRequest;
