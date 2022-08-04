import { Button, TextField } from "@mui/material";
import { store } from "../../../app/store";
import {
  fetchUsers,
  gatherUserDetails,
} from "../../slices/usersSlice/usersSlice";

export interface AddUserToRepoElementProps {}

export function AddUserToRepoElement(props: AddUserToRepoElementProps) {
  const gatherAction = () => {
    store.dispatch<any>(gatherUserDetails());
    store.dispatch<any>(fetchUsers());
  };

  return (
    <div>
      <TextField />
      <Button variant="contained" onClick={gatherAction}>
        Gather
      </Button>
    </div>
  );
}
