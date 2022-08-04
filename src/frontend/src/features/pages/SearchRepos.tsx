import { useLocation } from "react-router-dom";
import { ScanReposButton } from "../components/operations/ScanReposButton";
import { RepoList } from "../components/searchable/RepoList";

export interface SearchReposPageProps {}

export function SearchRepos(props: SearchReposPageProps) {
  const location = useLocation();
  console.log(location.pathname); // path is /contact

  console.log(location.pathname.split("/").length);
  const username = location.pathname.split("/").pop();
  console.log(username);

  return (
    <div>
      <ScanReposButton userName={username!} repoName={""} />
      <RepoList />
    </div>
  );
}
