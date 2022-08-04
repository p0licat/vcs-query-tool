import { Label } from "@mui/icons-material";
import { Button, TextField } from "@mui/material";
import { store } from "../../../app/store";
import ReposQueryParams from "../../slices/usersSlice/payloads/ReposQueryParams";
import ReposQueryParamsPayload from "../../slices/usersSlice/payloads/ReposQueryParamsPayload";
import {
  fetchRepos,
  setReposQueriesParams,
  updateRepoNameText,
} from "../../slices/usersSlice/reposSlice";

export interface ScanReposButtonProps {
  userName: string;
  repoName: string;
}

export function ScanReposButton(props: ScanReposButtonProps) {
  const handleTextChange = (event: any) => {
    let newValue = event.target.value;
    console.log(newValue);
    store.dispatch(updateRepoNameText({ newText: newValue })); // not an async thunk>?
  };

  const buttonAction = () => {
    const rq: ReposQueryParams = {
      userName: props.userName,
      repoName: props.repoName,
    };
    const rp: ReposQueryParamsPayload = {
      reposQueryParams: rq,
    };
    store.dispatch(setReposQueriesParams(rp));
    store.dispatch<any>(fetchRepos());
  };

  return (
    <div>
      <div>
        <TextField onChange={handleTextChange} />
        <Label>Filter</Label>
      </div>
      <div>
        <Button variant="contained" onClick={buttonAction}>
          Scan all repos
        </Button>
      </div>
    </div>
  );
}
