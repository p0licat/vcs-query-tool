import { List } from "@mui/material";

import { UserDTO } from "../model/dto/intermediate/UserDTO";
import { UserCard } from "../model/UserCard";
import { useNavigate } from "react-router-dom";

export interface ListUsersElementProps {
  userList: Array<UserDTO>;
}

export function ListUsersElement(props: ListUsersElementProps) {
  const navigate = useNavigate();
  const redirectCallback = (cardName: string) => {
    navigate("/search/" + cardName);
  };

  let componentList = props.userList.map((e: UserDTO) => {
    console.log(e.userName);
    return (
      <UserCard
        username={e.userName}
        fullname={e.fullName}
        url={e.reposUrl}
        redirectToSearchReposOfUser={redirectCallback}
      />
    );
  });

  return (
    <List sx={{ width: "100%", maxWidth: 360, bgcolor: "background.paper" }}>
      {componentList}
    </List>
  );
}
