import { List } from "@mui/material";

import { UserDTO } from "../model/dto/UserDTO";
import { UserCard } from "../model/UserCard";

export interface ListUsersElementProps {
  userList: Array<UserDTO>;
}

export function ListUsersElement(props: ListUsersElementProps) {
  let componentList = props.userList.map((e: UserDTO) => {
    return <UserCard name={e.fullName} url={e.reposUrl} />;
  });

  return (
    <List sx={{ width: "100%", maxWidth: 360, bgcolor: "background.paper" }}>
      {componentList}
    </List>
  );
}
