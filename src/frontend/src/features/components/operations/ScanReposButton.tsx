import { Button, TextField } from "@mui/material";
import { store } from "../../../app/store";
import ReposQueryParams from "../../slices/usersSlice/payloads/ReposQueryParams";
import ReposQueryParamsPayload from "../../slices/usersSlice/payloads/ReposQueryParamsPayload";
import {
  scanRepos,
  setReposQueriesParams,
  updateRepoNameText,
} from "../../slices/usersSlice/reposSlice";

import FilterAltIcon from "@mui/icons-material/FilterAlt";
import { RepoList } from "../searchable/RepoList";

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
    store.dispatch(setReposQueriesParams(rp)); // change this to not use RepoName (that is for contents only, the third route)
    store.dispatch<any>(scanRepos());
  };

  const loadReposAction = () => {};

  return (
    <div>
      <div>
        <TextField onChange={handleTextChange} />
        <FilterAltIcon fontSize="large" />
      </div>
      <div>
        <Button variant="contained" onClick={buttonAction}>
          Scan all repos
        </Button>
      </div>
      <div>
        <Button variant="contained" onClick={loadReposAction}>
          Load repos
        </Button>
        <RepoList />
      </div>
    </div>
  );
}
