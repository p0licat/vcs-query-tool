import { Button, TextField } from "@mui/material";
import { useAppSelector } from "../../../app/hooks";
import { store } from "../../../app/store";
import {
  codeFiles,
  refreshAllFilesContentsRequest,
  refreshAllRepoContentsRequest,
  searchCodeBase,
  updateCodeText,
} from "../../slices/codeSlice/codeSlice";
import { CodeResultList } from "./CodeResultList";

export interface SearchAllCodeTextProps {}

export function SearchAllCodeText(props: SearchAllCodeTextProps) {
  const codeFilesSelected = useAppSelector(codeFiles);

  const refreshContentsHandler = () => {
    store.dispatch<any>(refreshAllRepoContentsRequest()); // todo: test how many request this needs before implementing.
  };

  const collectContentsHandler = () => {
    store.dispatch<any>(refreshAllFilesContentsRequest());
  };

  const handleTextChange = (event: any) => {
    let newValue = event.target.value;
    store.dispatch(updateCodeText({ newText: newValue }));
  };

  const handleKeyDown = (event: any) => {
    let keyValue = event.key;
    if (keyValue === "Enter") {
      console.log("Enter key pressed");
      store.dispatch<any>(searchCodeBase());
    }
  };

  return (
    <div>
      <Button onClick={refreshContentsHandler}>Scan contents</Button>
      <Button onClick={collectContentsHandler}>Collect contents</Button>
      <TextField onChange={handleTextChange} onKeyDown={handleKeyDown} />
      <CodeResultList resultsList={codeFilesSelected} />
    </div>
  );
}
