#################################################################
#   DELEGATOR THAT PERFORMS THE DETAILS OF THE SEARCH PROCESS
################################################################
import time



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
    rank_score_t1 = rank_algorithm_time(tweet_1.get("tweetstamp"), tweet_1.get("likes"), tweet_1.get("retweets"))
    rank_score_t2 = rank_algorithm_time(tweet_2.get("tweetstamp"), tweet_2.get("likes"), tweet_2.get("retweets"))
    if(rank_score_t1 < rank_score_t2):
        return True
    return False



# comparator algorithm that computes rank score based on time
def rank_interest_compare(tweet_1, tweet_2):
    rank_score_t1 = rank_algorithm_interest(tweet_1.get("tweetstamp"), tweet_1.get("likes"), tweet_1.get("retweets"))
    rank_score_t2 = rank_algorithm_interest(tweet_2.get("tweetstamp"), tweet_2.get("likes"), tweet_2.get("retweets"))
    if(rank_score_t1 < rank_score_t2):
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
    if(isInterest):
        return sorted(filtered_tweets, cmp=make_comparator(rank_interest_compare), reverse=True)
    return sorted(filtered_tweets, cmp=make_comparator(rank_time_compare), reverse=True)




# given the time that tweet was posted, likes for tweet, and retweet count, calculate a score favoring time.
def rank_algorithm_time(time_posted, likecount,retweetcount):
    elapsed_sec = time.time() - time_posted
    # decay factor
    decayfactor_time = ((9.8/10.0)** (elapsed_sec/60)) # Retains 98% of previous iteration's value every 60 seconds.
    decayfactor_like = ((7.5/10.0)** (elapsed_sec/60)) # Retains 75% of previous iteration's value every 60 seconds.
    decayfactor_retweet = ((7.5/10.0)** (elapsed_sec/60)) # Retains 75% of previous iteration's value every 60 seconds.
    # calculated scores
    likescore = 500 * likecount * decayfactor_like
    retweetscore= 500 * retweetcount * decayfactor_retweet
    # add-one smoothing for recency. +1 for the current second, +0.5 after 2 second. + 0.33 after 3 seconds.
    time_priority_factor = 1.0/elapsed_sec
    timescore =  (3000 * decayfactor_time) + time_priority_factor
    # print "likescore", likescore, "retweetscore", retweetscore,"timescore", timescore
    return likescore + retweetscore + timescore


# given the time that tweet was posted, likes for tweet, and retweet count, calculate a score favoring likes and retweets.
def rank_algorithm_interest(time_posted, likecount,retweetcount):
    elapsed_sec = time.time() - time_posted
    # decay factor
    decayfactor_time = ((7.5/10.0)** (elapsed_sec/60)) # Retains 75% of previous iteration's value every 60 seconds.
    decayfactor_like = ((9.8/10.0)** (elapsed_sec/60)) # Retains 98% of previous iteration's value every 60 seconds.
    decayfactor_retweet = ((9.8/10.0)** (elapsed_sec/60)) # Retains 98% of previous iteration's value every 60 seconds.
    # calculated scores
    likescore = 1500 * likecount * decayfactor_like
    retweetscore= 1500 * retweetcount * decayfactor_retweet
    # add-one smoothing for recency. +1 for the current second, +0.5 after 2 second. + 0.33 after 3 seconds.
    time_priority_factor = 1.0/elapsed_sec
    timescore =  (1000 * decayfactor_time) + time_priority_factor
    # print "likescore", likescore, "retweetscore", retweetscore,"timescore", timescore
    return likescore + retweetscore + timescore



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
        "timestamp": tweet["tweetstamp"]
    })




# fills the search results tweet into the results' item field (array).
# breaks early if beyond the search limit or if tweets are repeated.
def fill_result_items(filtered_tweets, results,searchlimit):
    for tweet in filtered_tweets:
        if len(results["items"]) >= searchlimit:
            return results
        insert_tweet_nonrepeat(tweet,results)
    return results


#######################################################
#   SEARCH CONFIGURATION OBJECTS HELPERS
######################################################

# safechecks for parent field in search model, appends to config if found.
def modify_searchconfig_parentfield(searchConfig,searchmodel):
    if(searchmodel.parent):
        searchConfig["parent"] = searchmodel.parent
    # exclude tweets that are replies
    if(not searchmodel.replies):
        searchConfig["parent"] = None



####################################################
#   SEARCH : MAIN CONTROL FLOWS BATCHE QUERIES
####################################################

# search only the tweets of the users that this current user is following.
def search_following(loggedin_username, followsDB, tweetsDB, searchmodel, results):
    # get users logged in user is following
    following_users = followsDB.find({"follower_username": loggedin_username}).limit(searchmodel.limit)
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
            results =  fill_result_items(tweets,results, searchmodel.limit)
            return rank_result_tweets(results, is_rankfield_interest(searchmodel))





# don't filter by users that user is following
# if query string is specified
def search_not_following(tweetsDB, searchmodel,results):
    for word in searchmodel.q:
        if word != ".*":
            word = r"\b{}\b".format(word)
        searchConfig = {
            "content": {"$regex": word},
            "tweetstamp": {"$lte": searchmodel.tweetstamp}
        }
        modify_searchconfig_parentfield(searchConfig,searchmodel)
        tweets = tweetsDB.find(searchConfig).limit(searchmodel.limit)
        results = fill_result_items(tweets,results, searchmodel.limit)
        return rank_result_tweets(results, is_rankfield_interest(searchmodel))




# search only the tweets posted by a specific user of interest.
def search_username(tweetsDB, searchmodel,results):
    print 'filtering by username: ', searchmodel.username
    for word in searchmodel.q:
        if word != ".*":
            word = r"\b{}\b".format(word)
        searchConfig = {
            "username": searchmodel.username,
            "content": {"$regex": word},
            "tweetstamp": {"$lte": searchmodel.tweetstamp}
        }
        modify_searchconfig_parentfield(searchConfig,searchmodel)
        filtered_tweets = tweetsDB.find(searchConfig).limit(searchmodel.limit)
        results = fill_result_items(filtered_tweets,results, searchmodel.limit)
        return rank_result_tweets(results, is_rankfield_interest(searchmodel))