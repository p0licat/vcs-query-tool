import { Button } from "@mui/material";
import { AddUserToRepoElement } from "../components/operations/AddUserToRepoElement";
import { ListUsersElement } from "../components/searchable/ListUsersElement";

export interface UsersPageProps {}

export function Users(props: UsersPageProps) {
  return (
    <div>
      <div id="components">
        <AddUserToRepoElement />
        <Button variant="contained">Load users</Button>
        <ListUsersElement />
      </div>
      Users page.
    </div>
  );
}
