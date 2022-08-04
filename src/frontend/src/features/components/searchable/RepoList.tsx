import { List } from "@mui/material";

import { UserDTO } from "../model/dto/UserDTO";
import { UserCard } from "../model/UserCard";

export interface RepoListProps {}

export function RepoList(props: RepoListProps) {
  let componentList = null;
  return (
    <List sx={{ width: "100%", maxWidth: 360, bgcolor: "background.paper" }}>
      {componentList}
    </List>
  );
}
