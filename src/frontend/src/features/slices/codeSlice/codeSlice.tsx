import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";
import axios from "axios";
import { RootState } from "../../../app/store";
import CodeFileDTO from "../../components/model/dto/intermediate/CodeFileDTO";
import RefreshContentsDTO from "../../components/model/dto/RefreshContentsDTO";
import { SearchCodeDTO } from "../../components/model/dto/SearchCodeDTO";
import UpdateCodeSearchTextPayload from "../payloads/UpdateCodeSearchTextPayload";

export interface CodeSliceState {
  searchText: string;
  codeFiles: Array<CodeFileDTO>;
}

const initialState: CodeSliceState = {
  searchText: "",
  codeFiles: [],
};

export const refreshAllRepoContentsRequest = createAsyncThunk(
  "eventList/refreshContents",
  async () => {
    const response = await axios.post<RefreshContentsDTO>(
      `http://${process.env.REACT_APP_ADDRESS_OF_USER_DETAILS_ENDPOINT}:${process.env.REACT_APP_ADDRESS_OF_USER_DETAILS_ENDPOINT_PORT}/refreshContents`
    );
    return response.data;
  }
);

export const refreshAllFilesContentsRequest = createAsyncThunk(
  "eventList/refreshContents",
  async () => {
    const response = await axios.post<RefreshContentsDTO>(
      `http://${process.env.REACT_APP_ADDRESS_OF_USER_DETAILS_ENDPOINT}:${process.env.REACT_APP_ADDRESS_OF_USER_DETAILS_ENDPOINT_PORT}/refreshFileContents`
    );
    return response.data;
  }
);

export const searchCodeBase = createAsyncThunk(
  "eventList/searchCodeBase",
  async (_, { rejectWithValue, getState }) => {
    var stateResult: any = getState();
    var thisSlice = stateResult.codeSliceReducer;
    const searchText: string = thisSlice.searchText;
    const response = await axios.get<SearchCodeDTO>(
      `http://${process.env.REACT_APP_ADDRESS_OF_USER_DETAILS_ENDPOINT}:${process.env.REACT_APP_ADDRESS_OF_USER_DETAILS_ENDPOINT_PORT}/searchCode` +
        "?search=" +
        searchText
    );

    return response.data;
  }
);

export const codeSlice = createSlice({
  name: "code",
  initialState,
  reducers: {
    updateCodeText: (
      state,
      action: PayloadAction<UpdateCodeSearchTextPayload>
    ) => {
      state.searchText = action.payload.newText;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(searchCodeBase.pending, (state) => {})
      .addCase(searchCodeBase.fulfilled, (state, action) => {
        state.codeFiles = action.payload.contentsList;
      });
  },
});

export const { updateCodeText } = codeSlice.actions;
export const codeFiles = (state: RootState) => state.codeSliceReducer.codeFiles;
export default codeSlice.reducer;
