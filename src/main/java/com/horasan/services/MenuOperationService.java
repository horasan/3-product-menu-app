package com.horasan.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.WrapperQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.horasan.dto.MenuItem;

@Service
public class MenuOperationService {

    @Autowired
    private RestHighLevelClient elasticStackclient;
	
    @Value("${menu-tree-finder-index-name}")
    private String menuTreeFinderIndexName;
    
    @Value("${menu-tree-finder-highlighted-fields}")
    private String menuTreeFinderHighlightedFields;
    
    @Value("${highlight-pre-tag}")
    private String highlightPreTag;
    
    @Value("${highlight-post-tag}")
    private String highlightPostTag;
    
	public MenuItem createMenuItem(MenuItem menuItem) {
		// save to elastic search
		return menuItem;
	}
	
	
	public List<MenuItem> getAllMenuItems() throws IOException {

		List<MenuItem> menuItemList = new ArrayList<>();
		SearchRequest searchRequest2 = new SearchRequest(menuTreeFinderIndexName);
		
		String getAllQuery = 
				"{\r\n"
				+ "        \"match_all\": {}\r\n"
				+ "    }\r\n"
				+ "";

		WrapperQueryBuilder qb = QueryBuilders.wrapperQuery(getAllQuery);

		// add source builder.
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(qb);
		// can be used for pagination.
		sourceBuilder.from(0);
		sourceBuilder.size(5);
		sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

		searchRequest2.source(sourceBuilder);

		String highlighted = "";
		SearchResponse searchResponse = elasticStackclient.search(searchRequest2, RequestOptions.DEFAULT);
		for (SearchHit searchHit : searchResponse.getHits().getHits()) {
			MenuItem menuItem = new ObjectMapper().readValue(searchHit.getSourceAsString(), MenuItem.class);
			menuItem.setHitScore(searchHit.getScore());
			menuItem.setHighlighted(highlighted);
			menuItemList.add(menuItem);
		}

		return menuItemList;	
	}
	
	public List<MenuItem> queryDescriptionSearchAsYouType(String searchValue) throws IOException {

		List<MenuItem> menuItemList = new ArrayList<>();
		SearchRequest searchRequest2 = new SearchRequest(menuTreeFinderIndexName);
		
		String descriptionSearchAsYouTypeQuery = "{\r\n"
	        		+ "    \"multi_match\": {\r\n"
	        		+ "      \"query\": \"" + searchValue + "\",\r\n"
	        		+ "      \"type\": \"bool_prefix\",\r\n"
	        		+ "      \"fields\": [\r\n"                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
	        		+ "        \"description.shingles\",\r\n"
	        		+ "        \"description.shingles._3gram\",\r\n"
	        		+ "        \"description.shingles._4gram\",\r\n"
	        		+ "        \"description.shingles._index_prefix\",\r\n"
	        		+ "        \"description.ngrams\"\r\n"
	        		+ "      ]\r\n"
	        		+ "    }\r\n"
	        		+ "  },\r\n"
	        		+ "  \"highlight\" : {\r\n"
	        		+ "    \"fields\" : [\r\n"
	        		+ "      {\r\n"
	        		+ "        \"description.ngrams\": { } \r\n"
	        		+ "      }\r\n"
	        		+ "    ]\r\n"
	        		+ "  }";

	        
		WrapperQueryBuilder qb = QueryBuilders.wrapperQuery(descriptionSearchAsYouTypeQuery);

		// add source builder.
		SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
		sourceBuilder.query(qb);
		// can be used for pagination.
		sourceBuilder.from(0);
		sourceBuilder.size(10);
		sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

		// add highlighter to search query.
		HighlightBuilder highlightBuilder = new HighlightBuilder();
		HighlightBuilder.Field highlightTitle = new HighlightBuilder.Field(menuTreeFinderHighlightedFields);
		highlightTitle.highlighterType("unified");
		highlightTitle.preTags(highlightPreTag);
		highlightTitle.postTags(highlightPostTag);
		highlightBuilder.field(highlightTitle);

		sourceBuilder.highlighter(highlightBuilder);

		searchRequest2.source(sourceBuilder);

		String highlighted = "";
		SearchResponse searchResponse = elasticStackclient.search(searchRequest2, RequestOptions.DEFAULT);
		for (SearchHit searchHit : searchResponse.getHits().getHits()) {
			MenuItem menuItem = new ObjectMapper().readValue(searchHit.getSourceAsString(), MenuItem.class);
			menuItem.setHitScore(searchHit.getScore());

			if (!Objects.isNull(searchHit.getHighlightFields().get(menuTreeFinderHighlightedFields))) {

				// get highlighted fields.
				if (!Objects
						.isNull(((HighlightField) searchHit.getHighlightFields().get(menuTreeFinderHighlightedFields))
								.getFragments())) {
					highlighted = ((HighlightField) searchHit.getHighlightFields().get(menuTreeFinderHighlightedFields))
							.getFragments()[0].string();
				}
			}
			menuItem.setHighlighted(highlighted);
			menuItemList.add(menuItem);
		}

		return menuItemList;
	}

}
