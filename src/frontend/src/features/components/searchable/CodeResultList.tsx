import { List } from "@mui/material";
import { CodeFile } from "../model/CodeFile";
import CodeFileDTO from "../model/dto/intermediate/CodeFileDTO";

export interface CodeResultListProps {
  resultsList: Array<CodeFileDTO>;
}

export function CodeResultList(props: CodeResultListProps) {
  const reposListResult = props.resultsList;

  const componentsList = reposListResult?.map((e: CodeFileDTO) => {
    return (
      <div>
        <CodeFile
          fileName={e.fileName}
          contents={e.contents}
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
