import { Button, TextField } from "@mui/material";

export interface AddUserToRepoElementProps {}

export function AddUserToRepoElement(props: AddUserToRepoElementProps) {
  return (
    <div>
      <TextField />
      <Button variant="contained">Gather</Button>
    </div>
  );
}
