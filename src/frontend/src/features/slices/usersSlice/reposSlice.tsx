import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";
import axios from "axios";
import { RootState } from "../../../app/store";
import GetReposDTO from "../../components/model/dto/GetReposDTO";
import RepositoryDTO from "../../components/model/dto/RepositoryDTO";
import ReposQueryParams from "./payloads/ReposQueryParams";
import ReposQueryParamsPayload from "./payloads/ReposQueryParamsPayload";

export interface ReposSliceState {
  reposList: Array<RepositoryDTO>;
  reposQueryParams: null | ReposQueryParams;
}

const initialState: ReposSliceState = {
  reposList: [],
  reposQueryParams: null,
};

export const fetchRepos = createAsyncThunk("eventList/fetchUsers", async () => {
  const response = await axios.get<GetReposDTO>(
    `http://${process.env.REACT_APP_ADDRESS_OF_USER_DETAILS_ENDPOINT}:${process.env.REACT_APP_ADDRESS_OF_USER_DETAILS_ENDPOINT_PORT}/getRepos`
  );

  return response.data;
});

export const reposSlice = createSlice({
  name: "repos",
  initialState,
  reducers: {
    setReposQueriesParams: (
      state,
      action: PayloadAction<ReposQueryParamsPayload>
    ) => {
      state.reposQueryParams = action.payload.reposQueryParams;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchRepos.pending, (state) => {})
      .addCase(fetchRepos.fulfilled, (state, action) => {
        state.reposList = action.payload.reposList;
      });
  },
});

export const { setReposQueriesParams } = reposSlice.actions;
export const reposList = (state: RootState) =>
  state.reposSliceReducer.reposList;
export default reposSlice.reducer;
