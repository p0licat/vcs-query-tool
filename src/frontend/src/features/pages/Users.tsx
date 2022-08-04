import { Button } from "@mui/material";
import { useAppSelector } from "../../app/hooks";
import { store } from "../../app/store";
import { AddUserToRepoElement } from "../components/operations/AddUserToRepoElement";
import { ListUsersElement } from "../components/searchable/ListUsersElement";
import { fetchUsers, usersList } from "../slices/usersSlice/usersSlice";

export interface UsersPageProps {}

export function Users(props: UsersPageProps) {
  const userList = useAppSelector(usersList);

  const loadUsers = () => {
    console.log("load");
    store.dispatch<any>(fetchUsers());
  };

  return (
    <div>
      <div id="components">
        <AddUserToRepoElement />
        <Button variant="contained" onClick={loadUsers}>
          Load users
        </Button>
        <ListUsersElement userList={userList} />
      </div>
      Users page.
    </div>
  );
}
