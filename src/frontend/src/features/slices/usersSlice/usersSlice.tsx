import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import axios from "axios";
import { RootState } from "../../../app/store";
import { GetUsersDTO } from "../../components/model/dto/GetUsersDTO";
import { UserDTO } from "../../components/model/dto/UserDTO";

export interface UsersSliceState {
  usersList: Array<UserDTO>;
}

const initialState: UsersSliceState = {
  usersList: [],
};

export const fetchUsers = createAsyncThunk("eventList/fetchUsers", async () => {
  const response = await axios.get<GetUsersDTO>(
    `http://${process.env.REACT_APP_ADDRESS_OF_USER_DETAILS_ENDPOINT}:${process.env.REACT_APP_ADDRESS_OF_USER_DETAILS_ENDPOINT_PORT}/getUsers`
  );
  console.log(
    `http://${process.env.REACT_APP_ADDRESS_OF_USER_DETAILS_ENDPOINT}:${process.env.REACT_APP_ADDRESS_OF_USER_DETAILS_ENDPOINT_PORT}/getUsers`
  );
  console.log(response);
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
        state.usersList = action.payload.userList;
      });
  },
});

export const usersList = (state: RootState) =>
  state.usersSliceReducer.usersList;
export default usersSlice.reducer;
