import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";
import { RootState } from "../../../app/store";
import { UserDTO } from "../../components/model/dto/UserDTO";

export interface UsersSliceState {
  usersList: Array<UserDTO>;
}

const initialState: UsersSliceState = {
  usersList: [],
};

export const fetchUsers = createAsyncThunk("eventList/fetchUsers", async () => {
  const response = await axios.get<UserDTO>(
    `https://${process.env.REACT_APP_ADDRESS_OF_USER_DETAILS_ENDPOINT}:${process.env.REACT_APP_ADDRESS_OF_USER_DETAILS_ENDPOINT_PORT}/getUsers`
  );

  return response.data;
});

export const usersSlice = createSlice({
  name: "users",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchUsers.pending, (state) => {})
      .addCase(fetchUsers.fulfilled, (state, action) => {
        state.usersList = [action.payload];
      });
  },
});

export const usersList = (state: RootState) =>
  state.usersSliceReducer.usersList;
export default usersSlice.reducer;
