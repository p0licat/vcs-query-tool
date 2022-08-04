import { Button } from "@mui/material";
import { store } from "../../../app/store";
import ReposQueryParams from "../../slices/usersSlice/payloads/ReposQueryParams";
import ReposQueryParamsPayload from "../../slices/usersSlice/payloads/ReposQueryParamsPayload";
import {
  fetchRepos,
  setReposQueriesParams,
} from "../../slices/usersSlice/reposSlice";

export interface ScanReposButtonProps {
  userName: string;
  repoName: string;
}

export function ScanReposButton(props: ScanReposButtonProps) {
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
      <Button variant="contained" onClick={buttonAction}>
        Scan all repos
      </Button>
    </div>
  );
}
