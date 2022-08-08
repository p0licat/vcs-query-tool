// private Long id;
// String nodeId;
// String name;
// String description;
// String language;
// // urls
// String contentsUrl;
// String commitsUrl;
// String branchesUrl;
// // datetime
// String createdAt;
// String updatedAt;
// String pushedAt;

export default interface RepositoryDTO {
  id: number;
  nodeId: string;
  name: string;

  description: string;
  language: string;
  contentsUrl: string;
  commitsUrl: string;
  branchesUrl: string;
  createdAt: string;
  updatedAt: string;
  pushedAt: string;
}
