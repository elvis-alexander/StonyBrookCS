#################################################################
#   DELEGATOR THAT PERFORMS THE DETAILS OF THE SEARCH PROCESS
################################################################
import time
import pprint

#############################
#   SEARCH : RANKING
#############################


# checks what ranking algorithm to use based on the search model.
def is_rankfield_interest(searchmodel):
    if(searchmodel.rank.lower().strip() == "time"):
        return False
    return True


# comparator algorithm that computes rank score based on time
def rank_time_compare(tweet_1, tweet_2):
    tweet_1_score = tweet_1["timestamp"]
    tweet_2_score = tweet_2["timestamp"]
    if tweet_1_score < tweet_2_score:
        return True
    return False

# comparator algorithm that computes rank score based on time
def rank_interest_compare(tweet_1, tweet_2):
    tweet_1_score = tweet_1["likes"] + tweet_2["retweets"]
    tweet_2_score = tweet_2["likes"] + tweet_2["retweets"]
    if tweet_1_score < tweet_2_score:
        return True
    return False


# constructs a comparison function, by choosing which comparator algorithm function to use
def make_comparator(rank_comparator):
    def compare(x, y):
        if rank_comparator(x, y):
            return -1
        elif rank_comparator(y, x):
            return 1
        else:
            return 0
    return compare


# ranks all the tweets in the filtered list by priority algorithm
def rank_result_tweets(filtered_tweets, isInterest):
    filtered_tweets = filtered_tweets["items"]
    if isInterest == True:
        return sorted(filtered_tweets, cmp=make_comparator(rank_interest_compare), reverse=True)
    return sorted(filtered_tweets, cmp=make_comparator(rank_time_compare), reverse=True)


#############################
#   HELPER
#############################
# inserts the tweet into the results without repetition
def insert_tweet_nonrepeat(tweet,results):
    if tweet["content"] not in results["items"]:
        results["items"].append({
            "id": str(tweet["_id"]),
            "username": tweet["username"],
            "content": tweet["content"],
            "timestamp": tweet["tweetstamp"],
            "likes": tweet["likes"],
            "retweets": tweet["retweets"]
        })


# fills the search results tweet into the results' item field (array).
# breaks early if beyond the search limit or if tweets are repeated.
def fill_result_items(filtered_tweets, results, searchlimit, replies):
    for tweet in filtered_tweets:
        # if len(results["items"]) >= searchlimit:
        #     return results
        # exclude reply messages, if necessary
        is_reply = tweet.get("parent", None) != None
        if replies == False and is_reply == True:
            continue
        insert_tweet_nonrepeat(tweet,results)
    return results


#######################################################
#   SEARCH CONFIGURATION OBJECTS HELPERS
######################################################

# safechecks for parent field in search model, appends to config if found.
def modify_searchconfig_parentfield(searchConfig,searchmodel):
    if(searchmodel.parent != None):
        searchConfig["parent"] = searchmodel.parent
    # exclude tweets that are replies
    # i will exclude this for now...
    # if(not searchmodel.replies):
    #     searchConfig["parent"] = None


####################################################
#   SEARCH : MAIN CONTROL FLOWS BATCHE QUERIES, THIS CONTROL FLOW IS REALLY GOOD, GREAT JOB!
####################################################


# search only the tweets of the users that this current user is following.
def search_following(loggedin_username, followsDB, tweetsDB, searchmodel, results):
    # get users logged in user is following
    following_users = followsDB.find({"follower_username": loggedin_username})
    for user in following_users:
        # if query string is specified fix hereeeee
        for word in searchmodel.q:
            if word != ".*":
                word = r"\b{}\b".format(word)
            searchConfig = {
                "content": {"$regex": word},
                "username": user["username"],
                "tweetstamp": {"$lte": searchmodel.tweetstamp}
            }
            modify_searchconfig_parentfield(searchConfig,searchmodel)
            tweets = tweetsDB.find(searchConfig).limit(searchmodel.limit)
            results = fill_result_items(tweets,results, searchmodel.limit, searchmodel.replies)
            if len(results) >= searchmodel.limit:
                break
    ranked_results = rank_result_tweets(results, is_rankfield_interest(searchmodel))
    return {
        "status": "OK",
        "items": ranked_results[:searchmodel.limit]  # trim by limit
    }


# don't filter by users that user is following
# if query string is specified
def search_not_following(tweetsDB, searchmodel, results):
    for word in searchmodel.q:
        if word != ".*":
            word = r"\b{}\b".format(word)
        searchConfig = {
            "content": {"$regex": word},
            "tweetstamp": {"$lte": searchmodel.tweetstamp}
        }
        modify_searchconfig_parentfield(searchConfig,searchmodel)
        tweets = tweetsDB.find(searchConfig).limit(searchmodel.limit)
        results = fill_result_items(tweets,results, searchmodel.limit, searchmodel.replies)
        if len(results) >= searchmodel.limit:
            break
    ranked_results = rank_result_tweets(results, is_rankfield_interest(searchmodel))
    return {
        "status": "OK",
        "items": ranked_results[:searchmodel.limit]  # trim by limit
    }

# search only the tweets posted by a specific user of interest.
def search_username(tweetsDB, searchmodel, results):
    for word in searchmodel.q:
        if word != ".*":
            word = r"\b{}\b".format(word)
        searchConfig = {
            "username": searchmodel.username,
            "content": {"$regex": word},
            "tweetstamp": {"$lte": searchmodel.tweetstamp}
        }
        modify_searchconfig_parentfield(searchConfig,searchmodel)
        tweets = tweetsDB.find(searchConfig).limit(searchmodel.limit)
        results = fill_result_items(tweets,results, searchmodel.limit, searchmodel.replies)
        if len(results) >= searchmodel.limit:
            break
    ranked_results = rank_result_tweets(results, is_rankfield_interest(searchmodel))
    return {
        "status": "OK",
        "items": ranked_results[:searchmodel.limit] # trim by limit
    }