import { Button } from "@mui/material";
import { store } from "../../app/store";
import { AddUserToRepoElement } from "../components/operations/AddUserToRepoElement";
import { ListUsersElement } from "../components/searchable/ListUsersElement";
import { fetchUsers } from "../slices/usersSlice/usersSlice";

export interface UsersPageProps {}

export function Users(props: UsersPageProps) {
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
        <ListUsersElement />
      </div>
      Users page.
    </div>
  );
}
