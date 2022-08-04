import { List } from "@mui/material";
import { useAppSelector } from "../../../app/hooks";
import { reposList } from "../../slices/usersSlice/reposSlice";
import RepositoryDTO from "../model/dto/RepositoryDTO";

import { RepoCard } from "../model/RepoCard";

export interface RepoListProps {}

export function RepoList(props: RepoListProps) {
  const reposListResult = useAppSelector(reposList);
  const componentsList = reposListResult.map((e: RepositoryDTO) => {
    return (
      <div>
        <RepoCard
          repoName={""}
          userName={""}
          url={""}
          redirectToSearchRepoContents={() => {}}
        />
      </div>
    );
  });

  return (
    <List sx={{ width: "100%", maxWidth: 360, bgcolor: "background.paper" }}>
      {componentsList}
    </List>
  );
}
