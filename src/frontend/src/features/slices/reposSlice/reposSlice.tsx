import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";
import axios from "axios";
import { RootState } from "../../../app/store";
import GetReposDTO from "../../components/model/dto/GetReposDTO";
import RepositoryDTO from "../../components/model/dto/RepositoryDTO";
import ReposQueryParams from "../payloads/ReposQueryParams";
import ReposQueryParamsPayload from "../payloads/ReposQueryParamsPayload";
import UpdateRepoTextPayload from "../payloads/UpdateRepoTextPayload";

export interface ReposSliceState {
  reposList: Array<RepositoryDTO>;
  reposQueryParams: null | ReposQueryParams;
  repoName: string;
}

const initialState: ReposSliceState = {
  reposList: [],
  reposQueryParams: null,
  repoName: "",
};

export const scanRepos = createAsyncThunk(
  "eventList/scanRepos",
  async (_, { rejectWithValue, getState }) => {
    var stateResult: any = getState();
    var thisSlice = stateResult.reposSliceReducer;
    const userName: string = thisSlice.reposQueryParams.userName;
    //const repoName = thisSlice.reposQueryParams.repoName;
    const response = await axios.post<GetReposDTO>(
      `http://${process.env.REACT_APP_ADDRESS_OF_USER_DETAILS_ENDPOINT}:${process.env.REACT_APP_ADDRESS_OF_USER_DETAILS_ENDPOINT_PORT}/scanRepos` +
        "?username=" +
        userName
    );

    return response.data;
  }
);

export const fetchRepos = createAsyncThunk(
  "eventList/fetchRepos",
  async (_, { rejectWithValue, getState }) => {
    var stateResult: any = getState();
    var thisSlice = stateResult.reposSliceReducer;
    const userName: string = thisSlice.reposQueryParams.userName;
    //const repoName = thisSlice.reposQueryParams.repoName;
    const response = await axios.post<GetReposDTO>(
      `http://${process.env.REACT_APP_ADDRESS_OF_USER_DETAILS_ENDPOINT}:${process.env.REACT_APP_ADDRESS_OF_USER_DETAILS_ENDPOINT_PORT}/getRepos` +
        "?username=" +
        userName
    );

    return response.data;
  }
);

export const reposSlice = createSlice({
  name: "repos",
  initialState,
  reducers: {
    updateRepoNameText: (
      state,
      action: PayloadAction<UpdateRepoTextPayload>
    ) => {
      state.repoName = action.payload.newText;
    },
    setReposQueriesParams: (
      state,
      action: PayloadAction<ReposQueryParamsPayload>
    ) => {
      state.reposQueryParams = action.payload.reposQueryParams;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(scanRepos.pending, (state) => {})
      .addCase(scanRepos.fulfilled, (state, action) => {
        state.reposList = action.payload.repositories;
      })
      .addCase(fetchRepos.pending, (state) => {})
      .addCase(fetchRepos.fulfilled, (state, action) => {
        console.log("fulfilled fetch");
        console.log(action.payload);
        state.reposList = action.payload.repositories;
      });
  },
});

export const { updateRepoNameText } = reposSlice.actions;
export const { setReposQueriesParams } = reposSlice.actions;
export const reposList = (state: RootState) =>
  state.reposSliceReducer.reposList;
export default reposSlice.reducer;
