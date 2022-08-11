import { List } from "@mui/material";
import { CodeFile } from "../model/CodeFile";
import CodeFileDTO from "../model/dto/intermediate/CodeFileDTO";
import FileContents from "../model/dto/intermediate/FileContents";

export interface CodeResultListProps {
  resultsList: Array<CodeFileDTO>;
}

export function CodeResultList(props: CodeResultListProps) {
  const reposListResult = props.resultsList;

  const componentsList = reposListResult?.map((e: CodeFileDTO) => {
    let newContents: FileContents = {} as FileContents;
    const jsonObj = JSON.parse(e.contents);
    newContents.contents = jsonObj["contents"];
    return (
      <div>
        <CodeFile
          fileName={e.fileName}
          contents={newContents}
          url={e.url}
          // redirectToSearchRepoContents={() => {}} // todo
        />
      </div>
    );
  });

  return (
    <List sx={{ width: "100%", maxWidth: 360, bgcolor: "background.paper" }}>
      {componentsList}
    </List>
  );
}
