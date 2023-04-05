import Post from '../../views/forum/Post';
import BuildResponse from '../../views/builds/service/BuildResponse';

interface ForumResponse {
  post: Post,
  build: BuildResponse
}

export default ForumResponse;
