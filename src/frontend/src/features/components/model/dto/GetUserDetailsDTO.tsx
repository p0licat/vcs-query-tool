// userLogin	string
// id	integer($int64)
// nodeId	string
// subscriptionsUrl	string
// reposUrl	string

export default interface GetUserDetailsDTO {
  userLogin: string;
  id: number;
  nodeId: string;
  subscriptionsUrl: string;
  reposUrl: string;
}
