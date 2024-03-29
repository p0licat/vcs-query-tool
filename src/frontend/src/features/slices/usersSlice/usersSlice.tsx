import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";
import axios from "axios";
import { RootState } from "../../../app/store";
import GetUserDetailsDTO from "../../components/model/dto/GetUserDetailsDTO";
import { GetUsersDTO } from "../../components/model/dto/GetUsersDTO";
import { UserDTO } from "../../components/model/dto/intermediate/UserDTO";
import UpdateUserSearchTextPayload from "../payloads/UpdateUserSearchTextPayload";

export interface UsersSliceState {
  usersList: Array<UserDTO>;
  userSearchTextFieldValue: string;
}

const initialState: UsersSliceState = {
  usersList: [],
  userSearchTextFieldValue: "",
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

export const gatherUserDetails = createAsyncThunk(
  "eventList/gatherUserDetails",
  async (_, { rejectWithValue, getState }) => {
    var stateResult: any = getState();
    var thisSlice = stateResult.usersSliceReducer;
    console.log("cast ok");
    console.log(thisSlice.userSearchTextFieldValue);
    let userName = thisSlice.userSearchTextFieldValue;
    console.log(userName);
    const response = await axios.post<GetUserDetailsDTO>(
      `http://${process.env.REACT_APP_ADDRESS_OF_USER_DETAILS_ENDPOINT}:${process.env.REACT_APP_ADDRESS_OF_USER_DETAILS_ENDPOINT_PORT}/requestUserDetailsData?username=` +
        userName.toString()
    );

    return { dto: response };
  }
);

export const usersSlice = createSlice({
  name: "users",
  initialState,
  reducers: {
    updateUserSearchText: (
      state,
      action: PayloadAction<UpdateUserSearchTextPayload>
    ) => {
      state.userSearchTextFieldValue = action.payload.newText;
    },
  },
  extraReducers: (builder) => {
    builder
      .addCase(fetchUsers.pending, (state) => {})
      .addCase(fetchUsers.fulfilled, (state, action) => {
        state.usersList = action.payload.userList;
      });
  },
});

export const { updateUserSearchText } = usersSlice.actions;
export const usersList = (state: RootState) =>
  state.usersSliceReducer.usersList;
export default usersSlice.reducer;
