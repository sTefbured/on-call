import CollectionGrid from "../../../common/collectiongrid/CollectionGrid";
import JoinRequest from "./JoinRequest";

const JoinRequests = (props) => {
    let requestElements = props.joinGroupRequests.map(request =>
        <JoinRequest key={request.id} request={request}
                     approveRequest={() => props.approveRequest(request, props.page, props.pageSize)}/>)
    return (
        <CollectionGrid totalCount={props.totalCount}
                        pageSize={props.pageSize}
                        currentPage={props.page}
                        onPageChanged={n => props.setPage(n)}>
            {requestElements}
        </CollectionGrid>
    );
}

export default JoinRequests;