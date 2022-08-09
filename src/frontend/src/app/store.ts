import { Action, applyMiddleware, combineReducers, createStore, ThunkAction } from '@reduxjs/toolkit';
import { composeWithDevTools } from 'redux-devtools-extension';
import thunk from "redux-thunk";
import codeSlice from '../features/slices/codeSlice/codeSlice';
import reposSlice from '../features/slices/reposSlice/reposSlice';
import usersSlice from '../features/slices/usersSlice/usersSlice';

export const combinedstores = combineReducers({
  usersSliceReducer: usersSlice,
  reposSliceReducer: reposSlice,
  codeSliceReducer: codeSlice
});

export const store = createStore(
  combinedstores,
  composeWithDevTools(applyMiddleware(thunk)) // not needed in production
)

export type AppDispatch = typeof store.dispatch;
export type RootState = ReturnType<typeof store.getState>;
export type AppThunk<ReturnType = void> = ThunkAction<
  ReturnType,
  RootState,
  unknown,
  Action<string>
>;
