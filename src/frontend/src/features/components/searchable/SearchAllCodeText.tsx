import { TextField } from "@mui/material";
import { store } from "../../../app/store";
import {
  searchCodeBase,
  updateCodeText,
} from "../../slices/codeSlice/codeSlice";

export interface SearchAllCodeTextProps {}

export function SearchAllCodeText(props: SearchAllCodeTextProps) {
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
      <TextField onChange={handleTextChange} onKeyDown={handleKeyDown} />
    </div>
  );
}
