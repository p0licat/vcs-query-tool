import { Button, TextField } from "@mui/material";
import { store } from "../../../app/store";
import {
  fetchUsers,
  gatherUserDetails,
  updateUserSearchText,
} from "../../slices/usersSlice/usersSlice";

export interface AddUserToRepoElementProps {}

export function AddUserToRepoElement(props: AddUserToRepoElementProps) {
  const gatherAction = () => {
    store.dispatch<any>(gatherUserDetails());
    store.dispatch<any>(fetchUsers());
  };

  const handleTextChange = (event: any) => {
    let newValue = event.target.value;
    console.log(newValue);
    store.dispatch(updateUserSearchText({ newText: newValue })); // not an async thunk>?
  };

  return (
    <div>
      <TextField onChange={handleTextChange} />
      <Button variant="contained" onClick={gatherAction}>
        Gather
      </Button>
    </div>
  );
}
