// public String userLogin;
// public long id;
// public String nodeId;
// public String subscriptionsUrl;
// public String reposUrl;

// "id": 0,
// "fullName": null,
// "url": null,
// "reposUrl": "http://127.0.0.1:8080/scanReposOfUserOffline?username=p0licat",
// "followers_url": null,
// "nodeId": "asdf",
// "gitId": "12345"

export interface UserDTO {
  id: number;
  fullName: string;
  nodeId: string;
  gitId: string;
  followers_url: string;
  reposUrl: string;
  userName: string;
}
